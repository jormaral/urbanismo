package com.mitc.redes.editorfip.servicios.geometria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUsingJavaUtil {
	/*
	 * Zip function zip all files and folders
	 */
	
	public static boolean zipFiles(String srcFolder, String destZipFile) {
		boolean result = false;
		try {
			/*
			 * send to the zip procedure
			 */
			zipFolder(srcFolder, destZipFile);
			result = true;
		} catch (Exception e) {
		} finally {
			return result;
		}
	}

	/*
	 * zip the folders
	 */
	private static ZipOutputStream zip;
	
	private static void zipFolder(String srcFolder, String destZipFile)
			throws Exception {
		//ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		/*
		 * create the output stream to zip file result
		 */
		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(new FileOutputStream(destZipFile));
		/*
		 * add the folder to the zip
		 */
		addFolderToZip("", srcFolder, zip);
		/*
		 * close the zip objects
		 */
		zip.flush();
		zip.close();
	}

	/*
	 * recursively add files to the zip files
	 */
	private static void addFileToZip(String path, String srcFile, ZipOutputStream zip,
			boolean flag) throws Exception {
		/*
		 * create the file object for inputs
		 */
		File folder = new File(srcFile);

		/*
		 * if the folder is empty add empty folder to the Zip file
		 */
		if (flag == true) {
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
		} else { /*
				 * if the current name is directory, recursively traverse it to
				 * get the files
				 */
			if (folder.isDirectory()) {
				/*
				 * if folder is not empty
				 */
				addFolderToZip(path, srcFile, zip);
			} else {
				/*
				 * write the file to the output
				 */
				byte[] buf = new byte[1024];
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {
					/*
					 * Write the Result
					 */
					zip.write(buf, 0, len);
				}
			}
		}
	}

	/*
	 * add folder to the zip file
	 */
	private static void addFolderToZip(String path, String srcFolder,
			ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		/*
		 * check the empty folder
		 */
		if (folder.list().length == 0) {
			addFileToZip(path, srcFolder, zip, true);
		} else {
			/*
			 * list the files in the folder
			 */
			for (String fileName : folder.list()) {
				if (path.equals("")) {
					addFileToZip(folder.getName(), srcFolder + "/" + fileName,
							zip, false);
				} else {
					addFileToZip(path + "/" + folder.getName(), srcFolder + "/"
							+ fileName, zip, false);
				}
			}
		}
	}
}