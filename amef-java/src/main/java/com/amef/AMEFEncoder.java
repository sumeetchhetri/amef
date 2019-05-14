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
 * The AMEF Encoder Class
 * provides the encode method to encode the JDBObject
 */
public final class AMEFEncoder
{
	public byte[] encodeB(AMEFObject packet) throws AMEFEncodeException
	{
		return packet.getObjectRepr(true);
	}

	public byte[] encodeWL(AMEFObject packet) throws AMEFEncodeException
	{
		return packet.getObjectRepr(false);
	}
}
