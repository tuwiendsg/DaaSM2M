#!/bin/bash 

#create keyspace
echo "creating keyspace 'TestKeyspace'"
python ./createKeyspace.py TestKeyspace
sleep 1

#creates table "tableTestKeyspace" on keyspace "TestKeyspace" with PRIMARY KEY "key" of type "int", and columns "age(int), "name" (text) and "young"(boolean)
echo "creating table 'tableTestKeyspace'"
python ./createTable.py TestKeyspace tableTestKeyspace key int age int name text young boolean
sleep 1

#adds index to "age" column to be able to select based on age
echo "adding index on 'age' column"
python ./insertIndex.py TestKeyspace tableTestKeyspace age
sleep 1

echo "inserting 100 rows"
#add 100 rows to table
python ./insertRow.py TestKeyspace tableTestKeyspace key 2 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 3 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 4 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 5 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 6 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 7 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 8 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 9 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 10 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 11 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 12 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 13 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 14 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 15 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 16 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 17 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 18 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 19 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 20 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 21 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 22 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 23 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 24 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 25 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 26 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 27 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 28 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 29 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 30 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 31 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 32 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 33 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 34 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 35 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 36 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 37 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 38 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 39 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 40 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 41 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 42 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 43 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 44 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 45 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 46 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 47 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 48 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 49 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 50 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 51 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 52 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 53 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 54 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 55 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 56 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 57 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 58 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 59 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 60 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 61 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 62 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 63 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 64 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 65 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 66 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 67 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 68 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 69 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 70 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 71 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 72 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 73 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 74 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 75 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 76 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 77 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 78 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 79 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 80 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 81 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 82 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 83 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 84 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 85 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 86 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 87 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 88 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 89 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 90 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 91 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 92 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 93 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 94 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 95 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 96 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 97 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 98 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 99 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 100 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 101 age 3 name DDD young TRUE
python ./insertRow.py TestKeyspace tableTestKeyspace key 102 age 3 name DDD young TRUE

echo "reading 10 rows"
#read 10 rows first returned. NOTE that it appears the records are not returned in the order they where inserted
python ./readRow.py TestKeyspace tableTestKeyspace 10
echo "reading first 3 rows with age = 3"
python ./readRow.py TestKeyspace tableTestKeyspace 3 age = 3 

echo "reading 10 rows with key IN(92,97)"
#unfortunately IN works with sets not ranges. This means IN(92,97) represents where key = 92 || 97, not between 92,97
python ./readRow.py TestKeyspace tableTestKeyspace 10 "key IN(92,97)"
echo "deleting rows with key IN(92,97)"
#delete rows with key = 92 OR 97
python ./deleteRow.py TestKeyspace tableTestKeyspace "key IN(92,97)"

echo "reading 10 rows with key IN(92,97)"
#unfortunately IN works with sets not ranges. This means IN(92,97) represents where key = 92 || 97, not between 92,97
python ./readRow.py TestKeyspace tableTestKeyspace 10 "key IN(92,97)"
echo "dropping table tableTestKeyspace"
python ./dropTable.py TestKeyspace tableTestKeyspace

echo "dropping keyspace TestKeyspace"
python ./dropKeyspace.py TestKeyspace

