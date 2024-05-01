LIBRARY IEEE;
use IEEE.std_logic_1164.all;

	entity OutBufferControl is
	port (
		clk : in std_logic;
		RST : in std_logic;
		Load: in std_logic;
		ACK : in std_logic;
		Obfree: out std_logic;	
		Dval: out std_logic;	
		wreg: out std_logic
	);
end OutBufferControl;

architecture logic of OutBufferControl is
type STATE_TYPE is (STATE_FREE,STATE_SENDING, STATE_VAL, STATE_WAIT_ACK);

signal currentstate, nextstate: STATE_TYPE;

begin

	currentstate <= STATE_FREE when RST = '1' else nextstate when rising_edge(clk);
	
	GenerateNextState:
	process(currentstate, Load, ACK)
		Begin 
		case currentstate is
			when STATE_FREE    => if(load = '1') then
												nextstate <= STATE_SENDING;
									   	else
												nextstate <= STATE_FREE;
										   end if;
											
			when STATE_SENDING => if(load = '0') then 
												nextstate <= STATE_VAL;
											else 
												nextstate <= STATE_SENDING;
											end if;
												
			WHEN STATE_VAL =>  	if(ACK = '1') then
											nextstate <= STATE_WAIT_ACK;
										else
											nextstate <= STATE_VAL;
										end if;
											
											
			WHEN STATE_WAIT_ACK => if(ACK = '0') then
												nextstate <= STATE_FREE;
									   	else
												nextstate <= STATE_WAIT_ACK;
										   end if;

		end case;
	end process;
	
	Obfree <= '1' when currentstate = STATE_FREE else '0';
	Wreg <= '1' when (currentstate = STATE_SENDING and load = '1') else '0';
	Dval <= '1' when currentstate = STATE_VAL else '0';
	
end logic;
