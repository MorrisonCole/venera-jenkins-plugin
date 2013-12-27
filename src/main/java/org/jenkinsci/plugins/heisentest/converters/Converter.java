package org.jenkinsci.plugins.heisentest.converters;

public interface Converter<FROM, TO> {

	TO convert(final FROM from);
}
