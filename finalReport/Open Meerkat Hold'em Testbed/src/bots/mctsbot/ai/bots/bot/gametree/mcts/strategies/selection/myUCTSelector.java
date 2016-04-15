package bots.mctsbot.ai.bots.bot.gametree.mcts.strategies.selection;

import bots.mctsbot.ai.bots.bot.gametree.mcts.nodes.INode;
import bots.mctsbot.client.common.gamestate.GameState;
import bots.mctsbot.common.elements.player.PlayerId;

public class myUCTSelector extends MaxFunctionSelector {

	private final double C;

	public myUCTSelector(double C) {
		this.C = C;
	}

	@Override
	protected double evaluate(INode node) {
		int nbSamples = node.getNbSamples();
		if (nbSamples == 0)
			return 0;
		int nbParentSamples = node.getParent().getNbSamples();
		return node.getEV() + getC2(node) * Math.sqrt(Math.log(nbParentSamples) / nbSamples);
	}

	private double getC2(INode node) {
		GameState gameState = node.getGameState();
		PlayerId bot = node.getParent().bot;

		//wrong assumption if bot is already all-in and doesn't match opponents bets
		int maxProfit = gameState.getGamePotSize();

		int botStack = gameState.getPlayer(bot).getStack();
		//int botAmountCommitted = gameState.getPlayer(bot).getTotalInvestment();
		int botSpr = 0;

		if (maxProfit == 0) {
			botSpr = 1;
		}

		else {
			botSpr = botStack / maxProfit;
		}
		//so we don't divide by 0
		if (botSpr == 0) {
			botSpr = 1;
		}
		//return C  * (1 / botSpr);
		return C * (1 / botSpr);
	}
}
