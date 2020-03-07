package com.spirit.teresa.route;

public class DefaultRouterService implements RouterService {

    @Override
    public RouterInfo route(Object routeId) {
        return new RouterInfo("127.0.0.1",18866);
//        return new RouterInfo("10.19.85.79",18866);
    }

    @Override
    public void report(RouterInfo routeInfo, boolean success) {

    }
}
