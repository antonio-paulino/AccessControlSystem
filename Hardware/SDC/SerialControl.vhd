LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity SerialControl is
	port (
		RST 	: in std_logic; 
		clk 	: in std_logic;
		enRX 	: in std_logic; 
		accept: in std_logic;
		eq5 	: in std_logic;
		clr 	: out std_logic;
		wr 	: out std_logic;
		DXval : out std_logic
	);
end SerialControl;

architecture logic of SerialControl is
type STATE_TYPE is (STATE_WRITE, STATE_VAL, STATE_WAIT);

signal currentstate, nextstate: STATE_TYPE;

begin

	currentstate <= STATE_WAIT when RST = '1' else nextstate when rising_edge(clk);
	
	GenerateNextState:
	process(currentstate, enRX, eq5, accept)
		Begin 
		case currentstate is
			when STATE_WAIT    =>   if(enRX = '0') then
									 	   	nextstate <= STATE_WRITE;
									   	else
											   nextstate <= STATE_WAIT;
										   end if;
											
			when STATE_WRITE  =>    if (eq5 = '0' and enRx = '1') then
												nextstate <= STATE_WAIT;
											elsif(eq5 = '1' and enRx = '1') then
												nextstate <= STATE_VAL;
											else 
												nextstate <= STATE_WRITE;
											end if;
												
			WHEN STATE_VAL  =>    if(accept ='1') then
												nextstate <= STATE_WAIT;	
											else 
												nextstate <= STATE_VAL;
											end if;
		end case;
	end process;
	
	clr <= '1' when currentstate = STATE_WAIT else '0';
	wr <= '1' when (currentstate = STATE_WRITE and eq5 = '0' and enRx = '0') else '0';
	DXval <= '1' when currentstate = STATE_VAL else '0';
		
end logic;