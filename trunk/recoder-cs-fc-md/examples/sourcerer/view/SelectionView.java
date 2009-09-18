package sourcerer.view;

import sourcerer.*;

public interface SelectionView {

    SelectionModel getModel();
    void setModel(SelectionModel model);
    void modelUpdated(boolean selectionRemoved);
}
