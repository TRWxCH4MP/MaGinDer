package com.example.trw.maginder.callback;

import java.util.List;

/**
 * Created by _TRW on 21/1/2561.
 */

public interface ChooseMenuCallback {

    void ChooseMenuCallback(String menuId, String timeStampOrderMenu);

    void onMenuAmount(int menuCount);
}
