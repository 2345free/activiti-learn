/**
 * Copyright [b5cai.com] [tianyi]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * FileName:     BaseTest.java
 * @Description: TODO(用一句话描述该文件做什么)
 * All rights Reserved, Designed By luoxiaoxiao
 * Copyright:    Copyright(C) 2017-2017
 * Company       b5cai.com LTD.
 * @author:      tianyi
 * @version      V1.0
 * Createdate:   2018年1月31日 下午3:48:35
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年1月31日      luoxiaoxiao     1.0              1.0
 * Why & What is modified: <修改原因描述>
 */
package junit;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName:   BaseTest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author:      tianyi
 * @date:        2018年1月31日 下午3:48:35
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-*.xml")
public abstract class BaseTest {

}
