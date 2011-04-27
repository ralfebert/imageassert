package de.ralfebert.imageassert.compare;


/**
 * An ICompareResultHandler implementation is called when a PageImage does not
 * match and is responsible to handle this event.
 * 
 * @author Ralf Ebert
 */
public interface ICompareResultHandler {

	void onImageNotEqual(Page expected, Page actual);

}