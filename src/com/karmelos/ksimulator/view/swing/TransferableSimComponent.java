/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.view.swing;

import com.karmelos.ksimulator.model.SimComponent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *
 * @author MorpheuS
 */

class TransferableSimComponent implements Transferable {
  protected static final DataFlavor COMPONENT_FLAVOR = new DataFlavor(SimComponent.class, "A SimComponent Object");
  protected static final DataFlavor[] SUPPORTED_FLAVORS = { COMPONENT_FLAVOR };
  SimComponent component;
  public TransferableSimComponent(SimComponent color) {
    component = color;
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return SUPPORTED_FLAVORS;
  }
  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    if (flavor.equals(COMPONENT_FLAVOR) || flavor.equals(DataFlavor.stringFlavor))
      return true;
    return false;
  }

  @Override
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
    if (flavor.equals(COMPONENT_FLAVOR))
      return component;
    else if (flavor.equals(DataFlavor.stringFlavor))
      return component.getComponentName();
    else
      throw new UnsupportedFlavorException(flavor);
  }

   
}