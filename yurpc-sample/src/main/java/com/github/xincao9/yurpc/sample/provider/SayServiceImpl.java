/*
 * Copyright 2020 xincao9@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.xincao9.yurpc.sample.provider;

import com.github.xincao9.yurpc.sample.Say;
import com.github.xincao9.yurpc.sample.SayService;
import com.github.xincao9.yurpc.spring.boot.starter.YUProvider;

/**
 * 招呼服务实现
 *
 * @author xincao9@gmail.com
 */
@YUProvider
public class SayServiceImpl implements SayService {

    /**
     * 执行
     *
     * @param say 招呼
     * @return 招呼
     */
    @Override
    public Say perform(Say say) {
        return say;
    }

}
