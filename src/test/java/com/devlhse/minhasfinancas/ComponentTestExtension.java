package com.devlhse.minhasfinancas;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.extension.*;

public class ComponentTestExtension implements
        BeforeAllCallback, BeforeEachCallback, ParameterResolver, ExtensionContext.Store.CloseableResource{

    private static final int EMBEDDED_POSTGRES_PORT = 15432;

    private EmbeddedPostgres embeddedPostgres;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        embeddedPostgres = EmbeddedPostgres.builder()
                .setPort(EMBEDDED_POSTGRES_PORT)
                .start();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public void close() throws Throwable {
        embeddedPostgres.close();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return null;
    }
}
