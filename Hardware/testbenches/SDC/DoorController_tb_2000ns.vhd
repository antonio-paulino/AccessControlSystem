LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;

ENTITY DoorController_TB IS
END DoorController_TB;

ARCHITECTURE teste OF DoorController_TB IS


    COMPONENT DoorController
    PORT (
        clk      : IN std_logic;
        RST      : IN std_logic;
        Din      : IN std_logic_vector(4 DOWNTO 0);
        Dval     : IN std_logic;
        Sclose   : IN std_logic;
        Sopen    : IN std_logic;
        Psensor  : IN std_logic;
        OnOff    : OUT std_logic;
        Done     : OUT std_logic;
        Dout     : OUT std_logic_vector(4 DOWNTO 0)
    );
    END COMPONENT;

    constant MCLK_PERIOD: time:= 20 ns;
	 constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;
	
	
    signal clk_tb        : std_logic;
    signal RST_tb        : std_logic;
    signal Din_tb        : std_logic_vector(4 downto 0);
    signal Dval_tb       : std_logic;
    signal Sclose_tb     : std_logic;
    signal Sopen_tb      : std_logic;
    signal Psensor_tb    : std_logic;
    signal OnOff_tb      : std_logic;
    signal Done_tb       : std_logic;
    signal Dout_tb       : std_logic_vector(4 downto 0);

begin

    
    UUT: DoorController PORT MAP (
        clk      => clk_tb,
        RST      => RST_tb,
        Din      => Din_tb,
        Dval     => Dval_tb,
        Sclose   => Sclose_tb,
        Sopen    => Sopen_tb,
        Psensor  => Psensor_tb,
        OnOff    => OnOff_tb,
        Done     => Done_tb,
        Dout     => Dout_tb
    );

	 clk_gen : process
			Begin
				clk_tb <= '0';
				wait for MCLK_HALF_PERIOD;
				clk_tb <= '1';
				wait for MCLK_HALF_PERIOD;
			end process;


    stimulus: process
    begin
        
        RST_tb <= '1';
        Din_tb <= "00000";
        Dval_tb <= '0';
        Sclose_tb <= '0';
        Sopen_tb <= '0';
        Psensor_tb <= '0';

        WAIT FOR MCLK_PERIOD;

        RST_tb <= '0';
		  
		  --Fechar porta inicialmente
		  WAIT FOR MCLK_PERIOD;
		  Sclose_tb <= '1';
		  WAIT FOR MCLK_PERIOD*3;

        -- Teste 1: Porta abre e fecha sem interrupções, é testado se o sinal sopen afeta o fecho e se o sinal sclose afeta a abertura
		  Din_tb <= "11001"; -- Abrir com velocidade  10
        WAIT FOR MCLK_PERIOD;
		  Dval_tb <= '1';
		  WAIT FOR MCLK_PERIOD*2;
		  Sclose_tb <= '1';
		  WAIT FOR MCLK_PERIOD*3;
        Sopen_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        Sopen_tb <= '0';
		  Sclose_tb <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  Dval_tb <= '0';
		  WAIT FOR MCLK_PERIOD*5;
		  Din_tb <= "11000"; -- Fechar com velocidade 10
		  WAIT FOR MCLK_PERIOD;
		  Dval_tb <= '1';
		  WAIT FOR MCLK_PERIOD;
		  Sopen_tb <= '1';
        WAIT FOR MCLK_PERIOD*3;
        Sclose_tb <= '1';
		  WAIT FOR MCLK_PERIOD*3;
		  Dval_tb <= '0';
		  WAIT FOR MCLK_PERIOD*5;

        -- Teste 2: Porta abre e fecha com interrupção, existe interrupção depois da porta ter aberto outra vez.
		  sclose_tb <= '0';
		  sopen_tb <= '0';
		  Din_tb <= "11001"; -- Abrir com velocidade  10
		  WAIT FOR MCLK_PERIOD;
		  Dval_tb <= '1';
        WAIT FOR MCLK_PERIOD*4;
        Sopen_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        Sopen_tb <= '0';
        WAIT FOR MCLK_PERIOD*3;
		  Dval_tb <= '0';
		  WAIT FOR MCLK_PERIOD*5;
		  Din_tb <= "11000"; -- Fechar com velocidade 10
		  WAIT FOR MCLK_PERIOD;
		  Dval_tb <= '1';
        WAIT FOR MCLK_PERIOD*4;
        Psensor_tb <= '1';
		  WAIT FOR MCLK_PERIOD*5;
		  Sopen_tb <= '1';
		  WAIT FOR MCLK_PERIOD*5;--Esperar com porta aberta e interrupção 
		  Psensor_tb <= '0';
		  WAIT FOR MCLK_PERIOD*5;
		  Sclose_tb <= '1';
		  WAIT FOR MCLK_PERIOD;
		  Dval_tb <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  
		  
		  -- Teste 3: Porta abre e fecha com interrupção, não existe interrupção depois da porta ter aberto outra vez. Dval baixa antes de ser concluido o comando.
		  sclose_tb <= '0';
		  sopen_tb <= '0';
		  Dval_tb <= '1';
		  Din_tb <= "11001"; -- Abrir com velocidade  10
        WAIT FOR MCLK_PERIOD*2;
		  Dval_tb <= '0';
		  WAIT FOR MCLK_PERIOD*2;
        Sopen_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        Sopen_tb <= '0';
		  WAIT FOR MCLK_PERIOD*5;
		  Dval_tb <= '1';
		  Din_tb <= "11000"; -- Fechar com velocidade 10
        WAIT FOR MCLK_PERIOD*4;
        Psensor_tb <= '1';
		  Dval_tb <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  Psensor_tb <= '0';
		  WAIT FOR MCLK_PERIOD*2;
		  Sopen_tb <= '1';
		  WAIT FOR MCLK_PERIOD*5;
		  Sclose_tb <= '1';
		  WAIT FOR MCLK_PERIOD;
		  
		 WAIT;
	end process;
	
end teste;

        