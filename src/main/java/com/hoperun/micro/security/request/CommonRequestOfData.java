package com.hoperun.micro.security.request;

public class CommonRequestOfData<T> {
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString()
	{
	    StringBuilder builder = new StringBuilder();
	    builder.append("CommonRequestOfData [data=");
	    builder.append(data);
	    builder.append("]");
	    return builder.toString();
	}

}
