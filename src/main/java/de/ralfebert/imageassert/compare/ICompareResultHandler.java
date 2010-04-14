package de.ralfebert.imageassert.compare;

public interface ICompareResultHandler {

	void onImageNotEqual(PageImage expected, PageImage actual);

}
