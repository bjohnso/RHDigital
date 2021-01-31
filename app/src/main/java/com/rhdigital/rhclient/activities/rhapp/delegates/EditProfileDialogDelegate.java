package com.rhdigital.rhclient.activities.rhapp.delegates;

import com.rhdigital.rhclient.common.dto.UserFieldDto;

public interface EditProfileDialogDelegate {
    void onComplete(UserFieldDto userField);
    void onComplete(Boolean shouldSave);
}
