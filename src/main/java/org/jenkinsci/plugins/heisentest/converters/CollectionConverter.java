package org.jenkinsci.plugins.heisentest.converters;

import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.Collection;

import static com.google.common.collect.Collections2.transform;

public class CollectionConverter<FROM, TO> implements Converter<Collection<FROM>, Collection<TO>> {

	private final Converter<FROM, TO> converter;

	public CollectionConverter(Converter<FROM, TO> converter) {
		this.converter = converter;
	}

	public Collection<TO> convert(Collection<FROM> from) {
		return transform(from, new Function<FROM, TO>() {
			public TO apply(@Nullable FROM from) {
				return converter.convert(from);
			}
		});
	}
}
