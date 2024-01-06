package com.xayah.libsardine.report;

import com.xayah.libsardine.model.Multistatus;
import com.xayah.libsardine.util.SardineUtil;

import java.io.IOException;

public abstract class SardineReport<T>
{
	public String toXml() throws IOException
	{
		return SardineUtil.toXml(toJaxb());
	}

	public abstract Object toJaxb();

	public abstract T fromMultistatus(Multistatus multistatus);
}
