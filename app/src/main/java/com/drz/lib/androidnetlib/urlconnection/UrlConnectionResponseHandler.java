package com.drz.lib.androidnetlib.urlconnection;

import com.drz.lib.androidnetlib.entity.Header;

/**回调接口
 * @author drz
 *
 */
public interface UrlConnectionResponseHandler {
	/**请求成功的回调方法
	 * @param statusCode
	 * @param headers
	 * @param responseBody
	 */
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody);
	/**请求失败回调方法
	 * @param statusCode
	 * @param headers
	 * @param responseBody
	 * @param error
	 */
	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error);
}
