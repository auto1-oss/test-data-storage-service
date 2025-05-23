/*
 * This file is part of the auto1-oss/test-data-storage-service.
 *
 * (c) AUTO1 Group SE https://www.auto1-group.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.auto1.testdatastorage.utils;

/*-
 * #%L
 * test-data-storage-service
 * %%
 * Copyright (C) 2023 Auto1 Group
 * %%
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
 * #L%
 */

import com.auto1.testdatastorage.exception.EmptyQueueException;
import com.auto1.testdatastorage.exception.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ExceptionSupplier {

    public Supplier<NotFoundException> notFoundException(String entity, String key) {
        return () -> new NotFoundException(String.format("%s [%s] not found", entity, key));
    }

    public Supplier<EmptyQueueException> emptyQueueException(String omniType) {
        return () -> new EmptyQueueException(String.format("Queue with [%s] omni type is empty ", omniType));
    }

}
