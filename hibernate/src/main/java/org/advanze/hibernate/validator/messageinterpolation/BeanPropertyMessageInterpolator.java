package org.bitbucket.risu8.nuije.hibernate.validator.messageinterpolation;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class BeanPropertyMessageInterpolator extends ResourceBundleMessageInterpolator {

    private static final Pattern MESSAGE_PARAMETER_PATTERN = Pattern.compile( "(\\{[^\\}]+?\\})" );

    public BeanPropertyMessageInterpolator(MessageSource messageSource) {
        super(new MessageSourceResourceBundleLocator(messageSource));
    }

    @Override
    public String interpolate(String message, Context context, Locale locale) {
        String resolvedMessage = super.interpolate(message, context, locale);
        resolvedMessage = replacePropertyNameWithPropertyValues(resolvedMessage, context.getValidatedValue());
        return resolvedMessage;
    }

    private String replacePropertyNameWithPropertyValues(String resolvedMessage, Object validatedValue) {
        Matcher matcher = MESSAGE_PARAMETER_PATTERN.matcher( resolvedMessage );
        StringBuffer sb = new StringBuffer();

        while ( matcher.find() ) {
            String parameter = matcher.group( 1 );

            String propertyName = parameter.replace("{", "");
            propertyName = propertyName.replace("}", "");

            PropertyDescriptor desc = null;
            try {
                if (validatedValue == null) {
                    throw new IntrospectionException("Value of " + propertyName + " is null");
                }
                desc = new PropertyDescriptor(propertyName, validatedValue.getClass());
            } catch (IntrospectionException ignore) {
                matcher.appendReplacement( sb, parameter );
                continue;
            }

            try {
                Object propertyValue = desc.getReadMethod().invoke(validatedValue);
                matcher.appendReplacement( sb, propertyValue.toString() );
            } catch (Exception ignore) {
                matcher.appendReplacement( sb, parameter );
            }
        }
        matcher.appendTail( sb );
        return sb.toString();
    }

}

