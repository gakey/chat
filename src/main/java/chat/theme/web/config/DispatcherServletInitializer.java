package chat.theme.web.config;

import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * サーブレットの初期化をフック
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	/**
	 * @Configutationを付与した上位コンテキスト設定クラスを指定
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
	        return null;
	}

	/**
	 * 	@Configurationを付与したサーブレットコンテキスト設定クラスを指定
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
	        return new Class<?>[] { WebConfig.class };
	}

	/**
	 * サーブレットマッピング
	 * @return String[] マッピング配列
	 */
	@Override
	protected String[] getServletMappings() {
	        return new String[] { "/" };
	}

	/**
	 * 初期化パラメータ設定
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
	        registration.setInitParameter("dispatchOptionsRequest", "true");
	}
}
