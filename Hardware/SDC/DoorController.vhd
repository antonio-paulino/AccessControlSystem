LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity DoorController is
	port (
		clk 	: in std_logic;
		RST 	: in std_logic;
		Din	: in std_logic_vector(4 downto 0);
		Dval 	: in std_logic;
		Sclose: in std_logic;
		Sopen	: in std_logic;
		Psensor: in std_logic;
		OnOff	: out std_logic;
		Done	: out std_logic;
		Dout	: out std_logic_vector(4 downto 0)
	);
end DoorController;

architecture logic of DoorController is
type STATE_TYPE is (STATE_CLOSE, STATE_OPEN, STATE_WAIT, STATE_DONE);

signal currentstate, nextstate: STATE_TYPE;
signal OC, OPEN_CLOSE :std_logic;
begin
	OC <= Din(0);
	currentstate <= STATE_CLOSE when RST = '1' else nextstate when rising_edge(clk);
	
	GenerateNextState:
	process(currentstate, Sopen, Sclose, Psensor, Dval, oc)
		Begin 
		
		case currentstate is
		
			when STATE_WAIT    =>  	if(Dval = '1' and OC ='1') then
												nextstate <= STATE_OPEN;
											elsif(Dval = '1' and OC ='0') then
												nextstate <= STATE_CLOSE;
									   	else
											   nextstate <= STATE_WAIT;
										   end if;
												
			WHEN STATE_OPEN  =>    	if(Sopen ='1' and OC = '1') then
												nextstate <= STATE_DONE;
											elsif ( Sopen ='1' and OC = '0') then
												nextstate <= STATE_CLOSE;
											else 
												nextstate <= STATE_OPEN;
											end if;
												
			WHEN STATE_CLOSE =>		if(Psensor ='1') then
												nextstate <= STATE_OPEN;
											elsif (Sclose ='1') then
												nextstate <= STATE_DONE;
											else 
												nextstate <= STATE_CLOSE;
											end if;
												
												
			WHEN STATE_DONE =>		if(Dval = '1') then
												nextstate <= STATE_DONE;
											else 
												nextstate <= STATE_WAIT;
											end if;
		
		end case;
	end process;
	
	OnOff <= '1' when ((currentstate = STATE_OPEN and Sopen = '0') or (currentstate = STATE_CLOSE and Sclose = '0' and Psensor = '0')) else '0';
	
	OPEN_CLOSE <= '1' when currentstate = STATE_OPEN else '0';
	
	Dout <= Din(4) & Din(3) & Din(2) & Din(1) & OPEN_CLOSE;
	
	Done <= '1' when currentstate = STATE_DONE else '0';
	
	
end logic;