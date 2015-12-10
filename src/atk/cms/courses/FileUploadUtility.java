package atk.cms.courses;

import java.io.IOException;
import javax.servlet.http.Part;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * The Part interface gets header information of each file sent by browser
 * The file name and other meta information is contained in the header
 * @param filePart 
 * @return fileName
 */
@ManagedBean(name="fileUpload")
@SessionScoped
public class FileUploadUtility {
    
	private static String fileName;

	public static String setFileName(String fileName) {
		FileUploadUtility.fileName = fileName;
		return fileName;
	}
	
	public static String getFileName() {
		return fileName;
	}
	
	public static void uploadCourseFile(Part courseFile, String folderPath) throws IOException {
		courseFile.write(folderPath + getCourseFileName(courseFile));
    }
	
	public static String getCourseFileName(Part filePart) {
        
    	String header = filePart.getHeader("content-disposition");
        
    	if (header == null) {
    		return null;
    	}
    	
        for (String headerPart : header.split(";")) {
            
        	if (headerPart.trim().startsWith("filename")) {
        		return fileName = (headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", ""));
            }
        }
        return null;
    }
}