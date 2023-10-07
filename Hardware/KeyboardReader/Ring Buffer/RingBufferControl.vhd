LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity RingBufferControl is
	port (
		clk 	: in std_logic;
		RST 	: in std_logic;
		DAV	: in std_logic;
		CTS 	: in std_logic;
		full	: in std_logic;
		empty	: in std_logic;
		Wr		: out std_logic;
		selPG	: out std_logic;	
		Wreg	: out std_logic;	
		DAC	: out std_logic;	
		incput: out std_logic;	
		incget: out std_logic	
	);
end RingBufferControl;

architecture logic of RingBufferControl is
type STATE_TYPE is (STATE_WAIT_DAV, STATE_SETADDR, STATE_WRITE, STATE_SEND, STATE_WAIT_DAC, STATE_INCPUT, STATE_INCGET);

signal currentstate, nextstate: STATE_TYPE;

begin

	currentstate <= STATE_WAIT_DAV when RST = '1' else nextstate when rising_edge(clk);
	
	GenerateNextState:
	process(currentstate, DAV, full, empty, CTS)
		Begin 
		case currentstate is
			when STATE_WAIT_DAV    => if(DAV = '0' and empty ='0' and CTS = '1') then
													nextstate <= STATE_SEND;
											elsif(DAV = '1' and full ='1' and CTS = '1') then
													nextstate <= STATE_SEND;
											elsif(DAV = '1' and full ='0') then
													nextstate <= STATE_SETADDR;
									   	else
													nextstate <= STATE_WAIT_DAV;
										   end if;
												
			WHEN STATE_SEND  =>    if (cts = '1') then
												nextstate <= STATE_SEND;
											else 
												nextstate <= STATE_INCGET;
											end if;
												
			WHEN STATE_SETADDR =>  nextstate <= STATE_WRITE;
															
			WHEN STATE_WRITE =>	  nextstate <= STATE_WAIT_DAC;
			
			WHEN STATE_WAIT_DAC => if(DAV = '1') then
												nextstate <= STATE_WAIT_DAC;
										  else
												nextstate <= STATE_INCPUT;
										  end if;
												
			WHEN STATE_INCPUT =>  nextstate <= STATE_WAIT_DAV;
			
			WHEN STATE_INCGET =>  nextstate <= STATE_WAIT_DAV;

		end case;
	end process;
	wr <= '1' when currentstate = STATE_WRITE else '0';
	selPG <= '1' when (currentstate = STATE_WRITE or currentstate = STATE_SETADDR or currentstate = STATE_WAIT_DAC or currentstate = STATE_INCPUT) else '0';
	DAC <= '1' when currentstate = STATE_WAIT_DAC else '0';
	incput <= '1' when currentstate = STATE_INCPUT else '0';
	incget <= '1' when currentstate = STATE_INCGET else '0';
	Wreg <= '1' when currentstate = STATE_SEND else '0';
end logic;