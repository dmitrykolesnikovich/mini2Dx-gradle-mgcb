/*******************************************************************************
 * Copyright 2019 Thomas Cashman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.mgcb;

import org.junit.Assert;
import org.junit.Test;

public class RelativePathTest {
	private static final String WINDOWS_FILE_SEPERATOR = "\\";
	private static final String UNIX_FILE_SEPERATOR = "/";

	@Test
	public void testGetRelativePathWindows() {
		Assert.assertEquals("c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build", "C:\\Users\\Test\\build\\c.txt", WINDOWS_FILE_SEPERATOR));
		Assert.assertEquals("c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build\\", "C:\\Users\\Test\\build\\c.txt", WINDOWS_FILE_SEPERATOR));

		Assert.assertEquals("a/b/c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build", "C:\\Users\\Test\\build\\a\\b\\c.txt", WINDOWS_FILE_SEPERATOR));
		Assert.assertEquals("a/b/c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build\\", "C:\\Users\\Test\\build\\a\\b\\c.txt", WINDOWS_FILE_SEPERATOR));

		Assert.assertEquals("../assets/c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build", "C:\\Users\\Test\\assets\\c.txt", WINDOWS_FILE_SEPERATOR));
		Assert.assertEquals("../assets/c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build\\", "C:\\Users\\Test\\assets\\c.txt", WINDOWS_FILE_SEPERATOR));

		Assert.assertEquals("../assets/a/b/c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build", "C:\\Users\\Test\\assets\\a\\b\\c.txt", WINDOWS_FILE_SEPERATOR));
		Assert.assertEquals("../assets/a/b/c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build\\", "C:\\Users\\Test\\assets\\a\\b\\c.txt", WINDOWS_FILE_SEPERATOR));

		Assert.assertEquals("../../../drop/assets/a/b/c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build", "C:\\drop\\assets\\a\\b\\c.txt", WINDOWS_FILE_SEPERATOR));
		Assert.assertEquals("../../../drop/assets/a/b/c.txt", MgcbTask.getRelativePath(
				"C:\\Users\\Test\\build\\", "C:\\drop\\assets\\a\\b\\c.txt", WINDOWS_FILE_SEPERATOR));
	}

	@Test
	public void testGetRelativePathUnix() {
		Assert.assertEquals("c.txt", MgcbTask.getRelativePath(
				"/home/test/build", "/home/test/build/c.txt", UNIX_FILE_SEPERATOR));
		Assert.assertEquals("c.txt", MgcbTask.getRelativePath(
				"/home/test/build/", "/home/test/build/c.txt", UNIX_FILE_SEPERATOR));

		Assert.assertEquals("a/b/c.txt", MgcbTask.getRelativePath(
				"/home/test/build", "/home/test/build/a/b/c.txt", UNIX_FILE_SEPERATOR));
		Assert.assertEquals("a/b/c.txt", MgcbTask.getRelativePath(
				"/home/test/build/", "/home/test/build/a/b/c.txt", UNIX_FILE_SEPERATOR));

		Assert.assertEquals("../assets/c.txt", MgcbTask.getRelativePath(
				"/home/test/build", "/home/test/assets/c.txt", UNIX_FILE_SEPERATOR));
		Assert.assertEquals("../assets/c.txt", MgcbTask.getRelativePath(
				"/home/test/build/", "/home/test/assets/c.txt", UNIX_FILE_SEPERATOR));

		Assert.assertEquals("../assets/a/b/c.txt", MgcbTask.getRelativePath(
				"/home/test/build", "/home/test/assets/a/b/c.txt", UNIX_FILE_SEPERATOR));
		Assert.assertEquals("../assets/a/b/c.txt", MgcbTask.getRelativePath(
				"/home/test/build/", "/home/test/assets/a/b/c.txt", UNIX_FILE_SEPERATOR));

		Assert.assertEquals("../../../var/lib/test/a/b/c.txt", MgcbTask.getRelativePath(
				"/home/test/build", "/var/lib/test/a/b/c.txt", UNIX_FILE_SEPERATOR));
		Assert.assertEquals("../../../var/lib/test/a/b/c.txt", MgcbTask.getRelativePath(
				"/home/test/build/", "/var/lib/test/a/b/c.txt", UNIX_FILE_SEPERATOR));
	}
}
