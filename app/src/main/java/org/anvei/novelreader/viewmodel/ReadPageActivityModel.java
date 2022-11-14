package org.anvei.novelreader.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.anvei.novelreader.beans.WebsiteChapter;

public class ReadPageActivityModel extends ViewModel {
    private final MutableLiveData<WebsiteChapter> liveData = new MutableLiveData<>();

    public MutableLiveData<WebsiteChapter> getLiveData() {
        return liveData;
    }
}
