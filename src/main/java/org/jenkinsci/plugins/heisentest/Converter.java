package org.jenkinsci.plugins.heisentest;

public interface Converter<FROM, TO> {

	TO convert(final FROM from);
}
