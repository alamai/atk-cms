package atk.cms.courses;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

@ManagedBean(name="fileUpload")
@SessionScoped
public class FileUploadUtility {
    
	public static void uploadCourseFile(Part courseFile, String folderPath) throws IOException {
    	courseFile.write(folderPath + getCourseFileName(courseFile));
    }

    /**
     * The Part interface gets header information of each file sent by browser
     * The file name and other meta information is contained in the header
     * @param part 
     * @return filename
     */
    private static String getCourseFileName(Part part) {
        
    	for (String cd : part.getHeader("content-disposition").split(";")) {
            
    		if (cd.trim().startsWith("filename")) {
    			
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1);
            }
    		else {
    			return null;
    		}
        }
		return null;
    }
}