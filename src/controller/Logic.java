package controller;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import dictionary.Constant;
import dictionary.MemoryUnits;
import model.Directory;
import model.Files;
import model.Folder;

public class Logic {

	private Map<Directory, Long> folder = new HashMap<Directory, Long>();
	private Map<Directory, Long> files = new HashMap<Directory, Long>();
	private File[] drives;
	

	public Logic() {
		drives = findDrives();
	}
	
	public void start() {
		storeSizeOfAllFolders();
		print(sorting(folder),Constant.LENGTH,Constant.DIRECTORY_TYPE);
		print(sorting(files),Constant.LENGTH,Constant.FILES_TYPE);
	}
   
	public File[] findDrives() {
		return File.listRoots();
	}

	/**
	 * This method check number of partition of disk,
	 * and then calculate the size of each folder and file, 
	 */
	
	public void storeSizeOfAllFolders() {

		if (drives != null && drives.length > 0) {
			for (File aDrive : drives) {
				File[] children = aDrive.listFiles();
				if (children != null) {
					for (File subChildren : children) {
						if (subChildren.isDirectory()) {
							calculateSize(subChildren);
						} else {
							
							files.put(new Files(subChildren.getAbsolutePath(),subChildren.getName()), subChildren.length());
						}
					}
				}
			}
		}
	}

	/**
	 * @param file List of files which are sorted on the basis of their size.
	 * 
	 * @return file List of sorted files.
	 * 
	 */
	public Map<Directory, Long> sorting(Map<Directory, Long> file) {
		// now let's sort the directories in decreasing order of value
		Map<Directory, Long> sortedfiles = file.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		return sortedfiles;
	}

	/**
	 * @param file List of files which we have to print
	 * 
	 * @param length Number of files which you want to print
	 * 
	 * @param type It explain difference between file and folder
	 * */
	public void print(Map<Directory, Long> file, int length, String type) {
		System.out.println("\nBiggest " + type + " in /path/to/target:\n");
		file.entrySet().stream().limit(length).forEach(item -> System.out.println(
				".\t" + item.getKey() + "\t" + sizeMappingToMemoryUnits(new Double(item.getValue().toString()))));
	}

	/**
	 * @param folder FolderOfInterest of which we find size.
	 * 
	 * @return size It return the size of folder.
	 */
	public double calculateSize(File directory) {
		long size = 0;
		if (directory.listFiles() != null) {
			for (File file : directory.listFiles()) {
				if ((file.isFile())) {
					size += file.length();
					files.put(new Files(file.getAbsolutePath(),file.getName()), file.length());
				} else {
					size += calculateSize(file);
				}
			}
		}
		folder.put(new Folder(directory.getAbsolutePath(),directory.getName(),directory.isFile()), size);
		return size;
	}

	/**
	 * @param size Take size in bytes
	 * 
	 * @return string It returns the size of file with it respective units e.g (bytes to GB)
	 */
	public String sizeMappingToMemoryUnits(double size) {
		int unit = 0;
		double oneByte = 1024;
		while (size >= oneByte) {
			unit++;
			size = size / oneByte;
		}
		return "size  " + Constant.DF2.format(size) + "" + MemoryUnits.values()[unit];
	}
}
