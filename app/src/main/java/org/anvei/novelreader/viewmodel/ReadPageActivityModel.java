package org.anvei.novelreader.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.anvei.novelreader.beans.WebsiteChapterInfo;

public class ReadPageActivityModel extends ViewModel {
    private final MutableLiveData<WebsiteChapterInfo> liveData = new MutableLiveData<>();

    public MutableLiveData<WebsiteChapterInfo> getLiveData() {
        return liveData;
    }
}
