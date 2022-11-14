package org.anvei.novelreader.beans;

import java.sql.Date;

public interface Novel {
    String getNovelName();
    String getAuthor();
    Date getLastReadTime();
}
