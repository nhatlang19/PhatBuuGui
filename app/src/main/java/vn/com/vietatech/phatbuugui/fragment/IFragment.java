package vn.com.vietatech.phatbuugui.fragment;

import vn.com.vietatech.dto.Delivery;

interface IFragment {
    public Delivery getData();
    public void clearView();
    public void setFields(Delivery delivery);
}
