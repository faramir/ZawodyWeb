/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.datamodels;

import java.util.List;
import javax.faces.model.DataModel;

/**
 *
 * @author slawek
 */
public class PagedDataModel extends DataModel {

    private int totalRowsCount;
    private int rowIndex;
    private List list;

    public PagedDataModel(List list, int totalRowsCount) {
        this.list = list;
        this.totalRowsCount = totalRowsCount;
    }

    @Override
    public int getRowCount() {
        return totalRowsCount;
    }

    @Override
    public Object getRowData() {
        try {
            return list.get(getRowIndex());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getRowIndex() {
        return rowIndex % list.size();
    }

    @Override
    public Object getWrappedData() {
        return list;
    }

    @Override
    public boolean isRowAvailable() {
        try {
            return getRowIndex() < list.size();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public void setWrappedData(Object list) {
        this.list = (List) list;
    }
}
