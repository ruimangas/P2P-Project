ssh -f istple_seprs4@onelab3.warsaw.rd.tp.pl 'cd my_project; ant -Darg0=3000 runTestSearchItem'
ssh -f istple_seprs4@ple2.ipv6.lip6.fr 'cd my_project; ant -Darg0=3001 runIdle'
ssh -f istple_seprs4@ple1.dmcs.p.lodz.pl 'cd my_project; ant -Darg0=3002 runIdle'
sleep 6s
ssh istple_seprs4@prata.mimuw.edu.pl 'cd my_project; ant -Darg0=3003 runTestAddItem'
