package misc.common;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;


public class ClasspathBasedURIResolver implements URIResolver {
	
	private final URIResolver resolver;
	public ClasspathBasedURIResolver(URIResolver nextResolver) {
		this.resolver = nextResolver;
	}
	@Override
	public Source resolve(String href, String base) throws TransformerException {
		 final InputStream inStream = getClass().getResourceAsStream(href);
		 return new StreamSource(inStream);
	}
}
