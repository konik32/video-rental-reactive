package com.casumo.videorental;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Service
public class MockCredentialsProvider implements CredentialsProvider {
    public static final String MOCK_SIGNATURE = "test-signature";
    public static final String CLIENT_EMAIL = "email@localhost";

    @Override
    public Credentials getCredentials() throws IOException {
        ServiceAccountCredentials credentials = mock(ServiceAccountCredentials.class);
        given(credentials.sign(any(byte[].class))).willReturn(MOCK_SIGNATURE.getBytes());
        given(credentials.getAccount()).willReturn(CLIENT_EMAIL);
        return credentials;
    }
}
