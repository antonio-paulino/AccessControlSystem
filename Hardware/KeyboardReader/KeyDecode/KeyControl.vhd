LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity KeyControl is
	port (
		RST : in std_logic; 
		clk : in std_logic;
		Kpress : in std_logic;
		Kscan : out std_logic;
		Kack : in std_logic;
		Kval : out std_logic
	);
end KeyControl;

architecture logic of KeyControl is
type STATE_TYPE is (STATE_SCAN, STATE_VALIDATE, STATE_WAIT);

signal currentstate, nextstate: STATE_TYPE;

begin

	currentstate <= STATE_SCAN when RST = '1' else nextstate when rising_edge(clk);
	
	GenerateNextState:
	process(currentstate, Kack, Kpress)
		Begin 
		case currentstate is
			when STATE_SCAN    =>   	if(Kpress = '1') then
									 	   	nextstate <= STATE_VALIDATE;
									   	else
											nextstate <= STATE_SCAN;
										end if;
											
			when STATE_VALIDATE  =>  	if( Kack = '1') then
											nextstate <= STATE_WAIT;
										else 
											nextstate <= STATE_VALIDATE;
										end if;
												
			WHEN STATE_WAIT  =>    		if(Kpress = '0' and Kack = '0') then
											nextstate <= STATE_SCAN;
										else 
											nextstate <= STATE_WAIT;
										end if;
		
		end case;
	end process;
	
	Kscan <= '1' when (currentstate = STATE_SCAN and Kpress ='0') else '0';
	Kval <= '1' when (currentstate = STATE_VALIDATE) else '0';
		
end logic;