/** 
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.model.sca.commands;

import java.util.concurrent.Callable;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;

import gov.redhawk.model.sca.ScaModelPlugin;

/**
 * @since 14.0
 */
public abstract class ScaModelCommand extends AbstractCommand {

	public ScaModelCommand() {
		super("REDHAWK Model Protected Command", "REDHAWK Model Protected Command");
	}

	/**
	 * Run a writable command within the editing domain of the given EObject. If the EObject is not in an editing domain
	 * the command is run without one.
	 *
	 * @param context The object whose editing domain should be used
	 * @param command Command to run
	 */
	public static void execute(EObject context, Command command) {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(context);
		if (domain != null) {
			domain.getCommandStack().execute(command);
		} else {
			final TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Registry.INSTANCE.getEditingDomain("gov.redhawk.sca.editingDomain");
			if (editingDomain != null) {
				editingDomain.getCommandStack().execute(command);
			} else {
				command.execute();
			}
		}
	}

	/**
	 * Perform a write operation in a command within the editing domain of the given object.
	 *
	 * @param context The object whose editing domain should be used
	 * @param runnable The write operation to run
	 * @since 20.5
	 */
	public static void execute(EObject context, Runnable runnable) {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(context);
		domain.getCommandStack().execute(new ScaModelCommand() {

			@Override
			public void execute() {
				runnable.run();
			}
		});
	}

	/**
	 * Run a <b>read only</b> operation within the model. This ensure the state of the model is not altered during the
	 * read. See also {@link ScaModelCommandWithResult}.
	 *
	 * @param <T> The return type
	 * @param context The object whose editing domain should be used
	 * @param runnable The <b>read only</b> operation to run
	 * @return The optional return value
	 * @throws InterruptedException
	 */
	public static < T > T runExclusive(EObject context, RunnableWithResult<T> runnable) throws InterruptedException {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(context);
		if (domain != null) {
			return TransactionUtil.runExclusive(domain, runnable);
		} else {
			runnable.run();
			return runnable.getResult();
		}
	}

	/**
	 * Perform a read-only operation that returns a result in a command within the editing domain of the given object.
	 * Exceptions are logged, not thrown.
	 *
	 * @param context The object whose editing domain should be used
	 * @param callable The read-only operation to run
	 * @return The result of the callable
	 * @since 20.5
	 */
	public static < T > T runExclusive(EObject context, Callable<T> callable) {
		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(context);
		try {
			RunnableWithResult.Impl<T> runnableWithResult = new RunnableWithResult.Impl<T>() {
				@Override
				public void run() {
					try {
						T callableResult = callable.call();
						setResult(callableResult);
					} catch (Exception e) { // SUPPRESS CHECKSTYLE Logged
						ScaModelPlugin.logError("Error while executing model command", e);
					}
				}
			};
			domain.runExclusive(runnableWithResult);
			return runnableWithResult.getResult();
		} catch (InterruptedException e) {
			ScaModelPlugin.logError("Interrupted while executing command", e);
			return null;
		}
	}

	@Override
	protected boolean prepare() {
		return true;
	}

	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public void redo() {
		throw new UnsupportedOperationException("Redo not supported");
	}

}
