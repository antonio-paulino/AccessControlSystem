LIBRARY IEEE;
USE IEEE.std_logic_1164.all;

ENTITY counter4_tb IS
END counter4_tb;

ARCHITECTURE teste OF counter4_tb IS

    COMPONENT counter4_tb IS
        PORT (
            RST    : in std_logic;
            CE     : in std_logic;
            CLK    : in std_logic;
            Q      : out std_logic_vector(4 downto 0)
        );
    END COMPONENT;
	 
	 constant MCLK_PERIOD: time:= 20 ns;
	 constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

  
	signal RST_tb     : std_logic;
    signal CE_tb      : std_logic;
    signal CLK_tb     : std_logic;
    signal Q_tb       : std_logic_vector(4 downto 0);


	 Begin	 
    UUT : counter4_tb
        PORT MAP (
            RST     =>  RST_tb,
            CE      =>  CE_tb,
            CLK     =>  CLK_tb,
            Q       =>  Q_tb
        );

    clk_gen : process
	 Begin
			clk_tb <= '0';
			wait for MCLK_HALF_PERIOD;
			clk_tb <= '1';
			wait for MCLK_HALF_PERIOD;
		end process;
		
	stimulus: process
    Begin
		--Reset
		  RST_tb  <= '1';
		  CE_tb   <= '0';
		  WAIT FOR MCLK_PERIOD; 	  --RESET
		  RST_tb  <= '0';
		  CE_tb   <= '1';
		  WAIT FOR MCLK_PERIOD*16;  --Esperar um ciclo completo
		  CE_tb   <= '0';
          WAIT; 				   	 --Esperar com contagem desativada

    END PROCESS;

END teste;