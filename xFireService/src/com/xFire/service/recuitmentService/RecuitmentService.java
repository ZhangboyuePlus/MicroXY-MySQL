package com.xFire.service.recuitmentService;

import java.util.List;

import com.xFire.bean.recuitment.RecuitmentBean;

public interface RecuitmentService
{
	public List<RecuitmentBean> getRecuitmentTitle(int haveGotRecuitmentCount, int lineSize);
	public String getRecuitmentContentByPosition(int position);
}
