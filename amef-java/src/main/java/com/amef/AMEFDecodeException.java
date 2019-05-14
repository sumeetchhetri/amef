/*
	Copyright 2011, Sumeet Chhetri

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package com.amef;

/**
 * @author sumeetc
 * The AMEFDecodeException Class
 */
public class AMEFDecodeException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AMEFDecodeException()
	{
		super();
	}

	public AMEFDecodeException(String message)
	{
		super(message);
	}

	public AMEFDecodeException(Exception e)
	{
		super(e);
	}
}
