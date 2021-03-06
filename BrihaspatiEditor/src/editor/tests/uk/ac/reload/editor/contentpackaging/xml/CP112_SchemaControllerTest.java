
package uk.ac.reload.editor.contentpackaging.xml;

import java.io.File;

import uk.ac.reload.editor.contentpackaging.CP_EditorHandler;
import uk.ac.reload.editor.metadata.xml.MD11_SchemaController;
import uk.ac.reload.editor.metadata.xml.MD121_SchemaController;
import uk.ac.reload.editor.metadata.xml.MD122_SchemaController;
import uk.ac.reload.testsupport.FileSupport;




/**
 * CP112_SchemaControllerTest
 * 
 * @author Phillip Beauvoir
 * @version $Id: CP112_SchemaControllerTest.java,v 1.1 1998/03/26 15:50:37 ynsingh Exp $
 */
public class CP112_SchemaControllerTest extends CP111_SchemaControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        sc = new CP112_SchemaController();
    }
    
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ----------------------------- From CP_SchemaController --------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     * No Metadata SchemaController
     */
    public void testCopySchemaFilesToFolder1() throws Exception {
        File tmpFolder = FileSupport.getTempFolder("cp");
        File[] files = {
                new File(tmpFolder, "ims_xml.xsd"),
                new File(tmpFolder, "imscp_rootv1p1p2.xsd"),
        };
        itestCopySchemaFilesToFolder(files, tmpFolder);
    }
    
    /**
     * Default Metadata SchemaController
     */
    public void testCopySchemaFilesToFolder2() throws Exception {
        File tmpFolder = FileSupport.getTempFolder("cp");
        File[] files = {
                new File(tmpFolder, "ims_xml.xsd"),
                new File(tmpFolder, "imscp_rootv1p1p2.xsd"),
                new File(tmpFolder, "imsmd_v1p2p2.xsd")
        };
        
        CP_SchemaController cpsc = (CP_SchemaController)sc;
        cpsc.setMD_SchemaController(cpsc.getDefaultMD_SchemaController());
        itestCopySchemaFilesToFolder(files, tmpFolder);
    }

    /**
     * 1.2.1 Metadata SchemaController
     */
    public void testCopySchemaFilesToFolder3() throws Exception {
        File tmpFolder = FileSupport.getTempFolder("cp");
        File[] files = {
                new File(tmpFolder, "ims_xml.xsd"),
                new File(tmpFolder, "imscp_rootv1p1p2.xsd"),
                new File(tmpFolder, "imsmd_rootv1p2p1.xsd")
        };
        
        CP_SchemaController cpsc = (CP_SchemaController)sc;
        cpsc.setMD_SchemaController(new MD121_SchemaController());
        itestCopySchemaFilesToFolder(files, tmpFolder);
    }

    /**
     * 1.1 Metadata SchemaController
     */
    public void testCopySchemaFilesToFolder4() throws Exception {
        File tmpFolder = FileSupport.getTempFolder("cp");
        File[] files = {
                new File(tmpFolder, "ims_xml.xsd"),
                new File(tmpFolder, "imscp_rootv1p1p2.xsd"),
                new File(tmpFolder, "ims_md_rootv1p1.xsd")
        };
        
        CP_SchemaController cpsc = (CP_SchemaController)sc;
        cpsc.setMD_SchemaController(new MD11_SchemaController());
        itestCopySchemaFilesToFolder(files, tmpFolder);
    }

    // ---------------------------------------------------------------------------------------------
    
    public void testGetMetadataVersion() throws Exception {
        itestGetMetadataVersion(null);
        MD121_SchemaController mdSchemaController = new MD121_SchemaController();
        CP_SchemaController cpsc = (CP_SchemaController)sc;
        cpsc.setMD_SchemaController(mdSchemaController);
        itestGetMetadataVersion("IMS Metadata 1.2.1");
    }
    
    // ---------------------------------------------------------------------------------------------

    public void testGetMD_SchemaController() throws Exception {
        itestGetMD_SchemaController(null);
        MD121_SchemaController mdSchemaController = new MD121_SchemaController();
        CP_SchemaController cpsc = (CP_SchemaController)sc;
        cpsc.setMD_SchemaController(mdSchemaController);
        itestGetMD_SchemaController(mdSchemaController);
    }
    
    // ---------------------------------------------------------------------------------------------
    
    public void testGetDefaultMD_SchemaController() throws Exception {
        itestGetDefaultMD_SchemaController(MD122_SchemaController.class);
    }

    // ---------------------------------------------------------------------------------------------
    // ----------------------------- From SchemaController -----------------------------------------
    // ---------------------------------------------------------------------------------------------
    
    
    public void testGetVersion() {
        itestGetVersion(CP_EditorHandler.IMS_CONTENT_PACKAGING_1_1_2);
    }
    
    // ---------------------------------------------------------------------------------------------
    
    public void testGetRootElementName() {
        itestGetRootElementName("manifest");
    }
    
    // ---------------------------------------------------------------------------------------------
    
    public void testGetSchemaFile() {
        File file = itestGetSchemaFile();
        assertEquals("Wrong Schema File", CP112_SchemaController.fileSchemaCP1_1_2, file);
    }

    // ---------------------------------------------------------------------------------------------
    
}
