/**
 * Copyright 2013 Technische Universitaet Wien (TUW), Distributed Systems Group
 * E184
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package tests;

import at.ac.tuwien.dsg.daas.entities.Column;
import at.ac.tuwien.dsg.daas.entities.Keyspace;
import at.ac.tuwien.dsg.daas.entities.Table;
import at.ac.tuwien.dsg.daas.entities.TableQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 *
 * @Author Daniel Moldovan
 * @E-mail: d.moldovan@dsg.tuwien.ac.at
 *
 */
public class JSONOutput {

    public static void main(String[] main) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("./examples/");
        if (!file.exists()) {
            file.mkdir();
        }

        {
            Keyspace k = new Keyspace("DDD");
            mapper.writeValue(new File("./examples/keyspace.json"), k);

            {
                Table t = new Table(k, "tableName", "pk", "pkt");
                t.addColumn(new Column("columnName", "columnType"));

                mapper.writeValue(new File("./examples/table.json"), t);

            }

            {
                Table t = new Table();
                t.setKeyspace(k);
                t.setName("tableName");
                TableQuery query = new TableQuery();
                query.setTable(t);
                query.setCondition("WHERE name='bob' AND score >= 40");
                mapper.writeValue(new File("./examples/query.json"), query);

            }

        }
    }
}
