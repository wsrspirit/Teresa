package com.tencent.teresa.route;

public interface RouterService {
    RouterInfo route(Object routeId);

    void report(RouterInfo routeInfo, boolean success);
}
