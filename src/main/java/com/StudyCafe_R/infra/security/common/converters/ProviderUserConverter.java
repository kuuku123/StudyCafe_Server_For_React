package com.StudyCafe_R.infra.security.common.converters;

public interface ProviderUserConverter<T, R> {
    R convert(T t);
}
