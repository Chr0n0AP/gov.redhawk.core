/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK bulkioInterfaces.
 *
 * REDHAWK bulkioInterfaces is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK bulkioInterfaces is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
/*
 * WARNING: This file is generated from InPort.java.template.
 *          Do not modify directly.
 */
package bulkio;

import org.apache.log4j.Logger;

import org.ossie.component.RHLogger;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

import BULKIO.PortStatistics;
import BULKIO.PortUsageType;
import BULKIO.PrecisionUTCTime;
import BULKIO.StreamSRI;
import bulkio.InUShortStream;
import bulkio.StreamListener;

/**
 * 
 */
public class InUShortPort extends BULKIO.jni.dataUshortPOA implements InDataPort<BULKIO.dataUshortOperations,short[]> {

    /**
     * A class to hold packet data.
     * 
     */
    public class Packet extends DataTransfer < short[] > {

        public Packet(short[] data, PrecisionUTCTime time, boolean endOfStream, String streamID, StreamSRI H, boolean sriChanged, boolean inputQueueFlushed ) {
            super(data,time,endOfStream,streamID,H,sriChanged,inputQueueFlushed);
        };
    };

    public Object streamsMutex;

    private InPortImpl<short[]> impl;
    protected Map<String, InUShortStream> streams;
    protected Map<String, InUShortStream[]> pendingStreams;
    protected List<StreamListener<InUShortStream>> streamAdded = new LinkedList<StreamListener<InUShortStream>>();

    /**
     * 
     */
    public InUShortPort( String portName ) {
        this( portName, null, new bulkio.sri.DefaultComparator(), null );
    }

    public InUShortPort( String portName,
                       bulkio.sri.Comparator compareSRI ){
        this( portName, null, compareSRI, null );
    }

    public InUShortPort( String portName,
                        bulkio.sri.Comparator compareSRI,
                        bulkio.SriListener sriCallback
                       ) {
        this( portName, null, compareSRI, sriCallback );
    }

    public InUShortPort( String portName, Logger logger ) {
        this( portName, logger, new bulkio.sri.DefaultComparator(), null );
    }

    public InUShortPort( String portName,
                       Logger logger,
                       bulkio.sri.Comparator compareSRI,
                       bulkio.SriListener sriCallback ) {
        impl = new InPortImpl<short[]>(portName, logger, compareSRI, sriCallback, new UShortDataHelper());
        this.streamsMutex = new Object();
        this.streams = new HashMap<String, InUShortStream>();
        this.pendingStreams = new HashMap<String, InUShortStream[]>();
    }

    public Logger getLogger() {
        return impl.getLogger();
    }

    public void setLogger(Logger logger){
        impl.setLogger(logger);
    }

    public void setLogger(RHLogger logger)
    {
        impl.setLogger(logger);
    }

    /**
     * 
     */
    public void setSriListener(bulkio.SriListener sriCallback) {
        impl.setSriListener(sriCallback);
    }

    /**
     * 
     */
    public String getName() {
        return impl.getName();
    }

    /**
     * 
     */
    public void enableStats(boolean enable) {
        impl.enableStats(enable);
    }

    /**
     * 
     */
    public PortStatistics statistics() {
        return impl.statistics();
    }

    /**
     * 
     */
    public PortUsageType state() {
        return impl.state();
    }

    /**
     * 
     */
    public StreamSRI[] activeSRIs() {
        return impl.activeSRIs();
    }

    /**
     * 
     */
    public int getCurrentQueueDepth() {
        return impl.getCurrentQueueDepth();
    }

    /**
     * 
     */
    public int getMaxQueueDepth() {
        return impl.getMaxQueueDepth();
    }

    /**
     * Registers a listener for new streams
     */
    public void addStreamListener(StreamListener<InUShortStream> listener) {
        streamAdded.add(listener);
    }

    /**
     * Unregisters a listener for new streams
     */
    public void removeStreamListener(StreamListener<InUShortStream> listener) {
        streamAdded.remove(listener);
    }

    /**
     * 
     */
    public void setMaxQueueDepth(int newDepth) {
        impl.setMaxQueueDepth(newDepth);
    }

    /**
     * 
     */
    public void pushSRI(StreamSRI header) {
        synchronized (impl.sriUpdateLock) {
            if (!impl.currentHs.containsKey(header.streamID)) {
                if ( impl.sriCallback != null ) {
                    impl.sriCallback.newSRI(header);
                }
                impl.currentHs.put(header.streamID, new sriState(header, true));
                if (header.blocking) {
                    //If switching to blocking we have to set the semaphore
                    synchronized (impl.dataBufferLock) {
                        if (!impl.blocking) {
                                try {
                                    impl.queueSem.acquire(impl.workQueue.size());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                        }
                        impl.blocking = true;
                    }
                }
                this.createStream(header.streamID, header);
            } else {
                int eos_count = 0;
                synchronized (impl.dataBufferLock) {
                    for (DataTransfer<short[]> packet : impl.workQueue) {
                        if ((packet.streamID.equals(header.streamID)) && (packet.EOS)) {
                            eos_count+=1;
                        }
                    }
                }
          
                int additional_streams = 0;
                if (pendingStreams.containsKey(header.streamID)) {
                    additional_streams = 1 + pendingStreams.get(header.streamID).length;
                }
                if ((eos_count!=0) && (additional_streams == eos_count)) { // current and pending streams are all eos
                  this.createStream(header.streamID, header);
                } else {
                    StreamSRI oldSri = impl.currentHs.get(header.streamID).getSRI();
                    boolean cval = false;
                    if ( impl.sri_cmp != null ) {
                        cval = impl.sri_cmp.compare( header, oldSri );
                    }
                    if ( cval == false ) {
                        if ( impl.sriCallback != null ) {
                            impl.sriCallback.changedSRI(header);
                        }
                        impl.currentHs.put(header.streamID, new sriState(header, true));
                        if (header.blocking) {
                            //If switching to blocking we have to set the semaphore
                            synchronized (impl.dataBufferLock) {
                                if (!impl.blocking) {
                                        try {
                                            impl.queueSem.acquire(impl.workQueue.size());
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                }
                                impl.blocking = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     */
    public void pushPacket(short[] data, PrecisionUTCTime time, boolean eos, String streamID)
    {
        if (!_acceptPacket(streamID, eos)) {
            short[] empty_data = new short[0];
            impl.pushPacket(empty_data, time, eos, streamID);
            return;
        }
        impl.pushPacket(data, time, eos, streamID);
    }

    /**
     * 
     */
    public Packet getPacket(long wait)
    {
        DataTransfer<short[]> p = impl.getPacket(wait);
        if (p == null) {
            return null;
        } else {
            return new Packet(p.getData(), p.getTime(), p.getEndOfStream(), p.getStreamID(), p.getSRI(), p.sriChanged(), p.inputQueueFlushed());
        }
    }
  
    public InUShortStream getStream(String streamID)
    {
        InUShortStream stream = null;
        synchronized (this.streamsMutex) {
            if (streams.containsKey(streamID)) {
                return streams.get(streamID);
            }
        }
        return stream;
    }
  
    public InUShortStream[] getStreams()
    {
        InUShortStream[] retval = null;
        Iterator<InUShortStream> streams_iter = streams.values().iterator();
        synchronized (this.streamsMutex) {
            retval = new InUShortStream[streams.size()];
            int streams_idx = 0;
            while (streams_iter.hasNext()) {
                retval[streams_idx] = streams_iter.next();
                streams_idx++;
            }
        }
        return retval;
    }

    void createStream(String streamID, BULKIO.StreamSRI sri)
    {
        InUShortStream stream = new InUShortStream(sri, this);
        synchronized (this.streamsMutex) {
            if (!streams.containsKey(streamID)) {
                // New stream
                streams.put(streamID, stream);
            } else {
                // An active stream has the same stream ID; add this new stream to the
                // pending list
                if (!pendingStreams.containsKey(streamID)) {
                    pendingStreams.put(streamID, new InUShortStream[0]);
                }
                InUShortStream[] tmp_streams = Arrays.copyOf(pendingStreams.get(streamID), pendingStreams.get(streamID).length+1);
                tmp_streams[tmp_streams.length - 1] = stream;
                pendingStreams.replace(streamID, tmp_streams);
            }
            for (StreamListener<InUShortStream> listener : streamAdded) {
                listener.newStream(stream);
            }
        }
    }

    public InUShortStream getCurrentStream(float timeout)
    {
      // Prefer a stream that already has buffered data
      InUShortStream retval = null;
      synchronized (this.streamsMutex) {
        for (InUShortStream value : streams.values()) {
            if (value._hasBufferedData()) {
                if (retval == null) {
                    retval = value;
                } else {
                    if (bulkio.time.utils.compare(value._queue.peekFirst().T, retval._queue.peekFirst().T) < 0) {
                        retval = value;
                    }
                }
            }
        }
        if (retval != null) {
            return retval;
        }
      }
  
      // Otherwise, return the stream that owns the next packet on the queue,
      // potentially waiting for one to be received
      Packet packet = this.peekPacket(timeout);
      if (packet != null) {
        return getStream(packet.streamID);
      }

      return null;
    }
  
    public Packet peekPacket(float timeout)
    {
        int timeout_ms = (int)(timeout * 1000);
        DataTransfer<short[]> p = impl.peekPacket(timeout_ms);
        if (p != null) {
            return new Packet(p.getData(), p.getTime(), p.getEndOfStream(), p.getStreamID(), p.getSRI(), p.sriChanged(), p.inputQueueFlushed());
        }
        return null;
    }

    public String getRepid()
    {
        return BULKIO.dataUshortHelper.id();
    }

    public String getDirection()
    {
        return CF.PortSet.DIRECTION_PROVIDES;
    }

    public Packet fetchPacket(String streamID)
    {
        DataTransfer<short[]> p =  this.impl.fetchPacket(streamID);
        if (p == null) {
            return null;
        }
        return new Packet(p.getData(), p.getTime(), p.getEndOfStream(), p.getStreamID(), p.getSRI(), p.sriChanged(), p.inputQueueFlushed());
    }

    public boolean isStreamActive(String streamID)
    {
        synchronized (this.streamsMutex) {
            if (pendingStreams.containsKey(streamID)) {
                // The current stream has received an EOS
                return false;
            } else if (!streams.containsKey(streamID)) {
                // Unknown stream, presumably no SRI was received
                return false;
            }
            return true;
        }
    }

    public boolean isStreamEnabled(String streamID)
    {
        synchronized (this.streamsMutex) {
            if (!pendingStreams.containsKey(streamID)) {
                InUShortStream stream = streams.get(streamID);
                if (stream != null) {
                    if (!stream.enabled()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public InUShortStream[] getReadyStreams(int samples)
    {
        InUShortStream[] retval = null;
        List<InUShortStream> stream_list = new ArrayList<InUShortStream>();
        Iterator<InUShortStream> streams_iter = streams.values().iterator();
        synchronized (this.streamsMutex) {
            while (streams_iter.hasNext()) {
                InUShortStream _stream = streams_iter.next();
                if (samples == 0) {
                    if (_stream.ready()) {
                        stream_list.add(_stream);
                    }
                } else {
                    if (_stream.samplesAvailable() >= samples) {
                        stream_list.add(_stream);
                    }
                }
            }
            int streams_idx = 0;
            retval = new InUShortStream[stream_list.size()];
            stream_list.toArray(retval);
        }
        return retval;
    }
  
    protected boolean _acceptPacket(String streamID, boolean EOS)
    {
        // Acquire streamsMutex for the duration of this call to ensure that
        // end-of-stream is handled atomically for disabled streams
        synchronized (this.streamsMutex) {
            // Find the current stream for the stream ID and check whether it's
            // enabled
            if (this.streams.get(streamID) == null) {
                return true;
            }
            if (this.streams.get(streamID).enabled()) {
                return true;
            }
    
            // If there's a pending stream, the packet is designated for that
            if (pendingStreams.get(streamID) != null) {
                return true;
            }
    
            if (EOS) {
                // Acknowledge the end-of-stream by removing the disabled stream
                // before discarding the packet
                this.streams.get(streamID)._close();
                streams.remove(streamID);

                InUShortStream[] _pS = pendingStreams.get(streamID);
                if (_pS != null) {
                    streams.put(streamID, _pS[0]);
                    InUShortStream[] tmp_streams = Arrays.copyOfRange(pendingStreams.get(streamID), 1, pendingStreams.get(streamID).length);
                    pendingStreams.replace(streamID, tmp_streams);
        
                }
            }
        }
        return false;
    }

    protected void _discardPacketsForStream(String streamID)
    {
        impl.discardPacketsForStream(streamID);
    }

    protected void _removeStream(String streamID)
    {
        synchronized (this.streamsMutex) {
            // Remove the current stream, and if there's a pending stream with the same
            // stream ID, move it to the active list
            InUShortStream value = streams.get(streamID);
            if (value != null) {
                value._close();
                streams.remove(streamID);
            }
            InUShortStream[] _pS = pendingStreams.get(streamID);
            if (_pS != null) {
                if (_pS.length > 0) {
                    streams.put(streamID, _pS[0]);
                    InUShortStream[] tmp_streams = Arrays.copyOfRange(pendingStreams.get(streamID), 1, pendingStreams.get(streamID).length);
                    pendingStreams.replace(streamID, tmp_streams);
                }
            }
        }
    }
    
}
