/*
 * Copyright 2020 Leo
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xin.wanandroid.ui.mine

import android.view.View
import androidx.lifecycle.Observer
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.MessageDialog
import com.xin.wanandroid.R
import com.xin.wanandroid.base.BaseVMFragment
import com.xin.wanandroid.core.Constant
import com.xin.wanandroid.ext.*
import com.xin.wanandroid.ui.collect.CollectActivity
import com.xin.wanandroid.ui.login.LoginActivity
import com.xin.wanandroid.ui.rank.PersonRankActivity
import com.xin.wanandroid.ui.rank.RankScoreActivity
import com.xin.wanandroid.ui.setting.SettingActivity
import com.xin.wanandroid.ui.webview.WebViewActivity
import com.xin.wanandroid.util.LiveBus
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *@author : Leo
 *@date : 2020/9/10 9:49
 *@since : xinxiniscool@gmail.com
 *@desc :
 */
class MineFragment : BaseVMFragment<MineViewModel>() {

    override fun getViewModelClass(): Class<MineViewModel> = MineViewModel::class.java

    override fun initLayoutView(): Int = R.layout.fragment_mine

    override fun initEvent() {
        loginStatus(isLogin)
        clMineLogin.setOnNoRepeatClickListener {
            mActivity.startActivity<LoginActivity>()
        }
        rlMineScores.setOnNoRepeatClickListener {
            mActivity.checkStartActivity<PersonRankActivity>()
        }
        rlRankScores.setOnNoRepeatClickListener {
            mActivity.checkStartActivity<RankScoreActivity>()
        }
        rlMineCollect.setOnNoRepeatClickListener {
            mActivity.checkStartActivity<CollectActivity>()
        }
        rlMineWebsite?.setOnNoRepeatClickListener {
            mActivity.startActivity<WebViewActivity> {
                putExtra(Constant.ARTICLE_TITLE, "WanAndroid")
                putExtra(Constant.ARTICLE_URL, "https://wanandroid.com")
            }
        }
        rlSetting.setOnNoRepeatClickListener {
            mActivity.startActivity<SettingActivity>()
        }
    }

    override fun initData() {
        LiveBus.observe<Boolean>(Constant.LOGIN_STATUS, this, Observer {
            loginStatus(it)
        })
    }

    override fun lazyLoadData() {
    }

    private fun loginStatus(isLogin: Boolean) {
        if (isLogin) {
            clMineLogin.isEnabled = false
            tvMineLogin.visibility = View.GONE
            tvMineName.run {
                text = user?.username
                visibility = View.VISIBLE
            }
            tvMineId.run {
                val id = "ID: ${user?.id}"
                text = id
                visibility = View.VISIBLE
            }
            rlLogout.run {
                setOnNoRepeatClickListener {
                    logoutDialog()
                }
                visibility = View.VISIBLE
            }
        } else {
            clMineLogin.isEnabled = true
            tvMineLogin.visibility = View.VISIBLE
            tvMineName.visibility = View.GONE
            tvMineId.visibility = View.GONE
            rlLogout.visibility = View.GONE
        }
    }

    private fun logoutDialog() {
        MessageDialog.build(mActivity)
            .setStyle(DialogSettings.STYLE.STYLE_IOS)
            .setTitle("小鑫提示")
            .setMessage("确定退出吗?")
            .setOkButton("确定") { baseDialog, _ ->
                mViewModel.logout()
                baseDialog.doDismiss()
                false
            }
            .setCancelButton("取消") { baseDialog, _ ->
                baseDialog.doDismiss()
                false
            }.show()
    }
}