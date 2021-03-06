/**
 *  RELOAD TOOLS
 *
 *  Copyright (c) 2004 Oleg Liber, Bill Olivier, Phillip Beauvoir
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  Project Management Contact:
 *
 *  Oleg Liber
 *  Bolton Institute of Higher Education
 *  Deane Road
 *  Bolton BL3 5AB
 *  UK
 *
 *  e-mail:   o.liber@bolton.ac.uk
 *
 *
 *  Technical Contact:
 *
 *  Phillip Beauvoir
 *  e-mail:   p.beauvoir@bolton.ac.uk
 *
 *  Web:      http://www.reload.ac.uk
 *
 */

package uk.ac.reload.editor.learningdesign.datamodel.components.environments;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jdom.Element;

import uk.ac.reload.dweezil.gui.ShortcutIcon;
import uk.ac.reload.dweezil.util.DweezilUIManager;
import uk.ac.reload.editor.datamodel.IDataComponentIcon;
import uk.ac.reload.editor.learningdesign.datamodel.LD_ComponentRef;
import uk.ac.reload.editor.learningdesign.datamodel.LD_DataModel;

/**
 * Learning Design Environment Ref Element
 *
 * @author Phillip Beauvoir
 * @version $Id: EnvironmentRef.java,v 1.1 1998/03/26 15:42:28 ynsingh Exp $
 */
public class EnvironmentRef
extends LD_ComponentRef
implements IDataComponentIcon
{
    private static final String DESCRIPTION = "A reference to an Environment.";

    /**
     * Constructor
     * @param ldDataModel
     * @param element
     */
    public EnvironmentRef(LD_DataModel ldDataModel, Element element) {
        setDataModel(ldDataModel);
        setElement(element);
        setDescription(DESCRIPTION);
    }

    /* (non-Javadoc)
     * @see uk.ac.reload.editor.learningdesign.datamodel.ILD_IconInterface#getIcon()
     */
    public Icon getIcon() {
        ImageIcon icon = DweezilUIManager.getIcon(ICON_ENVIRONMENT);
        return new ShortcutIcon(icon);
    }
    
    /**
     * Move the bound element up.
     * Over-ride this.  Because we've put the EnvironmentRefs in subgroups, moving the element up one place
     * doesn't necessarily mean it will move above the previous sibling of the same type.
     */
    protected void moveElementUp() {
        moveElementUpSameType();
    }
    
    /**
     * Move the bound element down.
     * Over-ride this.  Because we've put the EnvironmentRefs in subgroups, moving the element down one place
     * doesn't necessarily mean it will move below the next sibling of the same type.
     */
    protected void moveElementDown() {
        moveElementDownSameType();
    }

}
