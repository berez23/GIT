/*
 * LoggingWithAspectJ - Logging with AspectJ
 * Copyright (C) 2007 Christian Schenk
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package it.webred.aspectj;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * This indented logging aspect was inspired by <a
 * href="http://www.bibsonomy.org/bibtex/2684fcd95b8bce37858bcc13753047a7e/cschenk">AspectJ
 * in Action</a> (p. 171).
 * 
 * @author Christian Schenk
 */
@Aspect
@Component
public abstract class AbstractLoggingAspect {

	/** Indent width */
	private final int width = 2;
	/** Current indent with counter */
	private int indent = 0;

	/**
	 * This will be implemented by logging aspects.
	 */
	@Pointcut
	protected abstract void logging();

	@Before("logging()")
	public void increaseIndent() {
		this.indent++;
	}

	@After("logging()")
	public void decreaseIndent() {
		this.indent--;
	}

	@Before("call(* java.io.PrintStream.println(..))")
	public void print() {
		for (int spaces = 0, indent = this.indent * this.width; spaces < indent; spaces++) {
			System.out.print(" ");
		}
	}
}