package net.corda.examples.stockexchange.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.r3.corda.lib.tokens.contracts.states.FungibleToken;
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType;
import com.r3.corda.lib.tokens.contracts.types.TokenPointer;
import com.r3.corda.lib.tokens.contracts.types.TokenType;
import com.r3.corda.lib.tokens.money.FiatCurrency;
import com.r3.corda.lib.tokens.workflows.utilities.QueryUtilitiesKt;
import net.corda.core.contracts.Amount;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.utilities.ProgressTracker;
import net.corda.examples.stockexchange.flows.utilities.QueryUtilities;
import net.corda.examples.stockexchange.states.StockState;

public class QueryStock {

    @InitiatingFlow
    @StartableByRPC
    public static class GetStockBalance extends FlowLogic<Amount<TokenType>> {
        private final ProgressTracker progressTracker = new ProgressTracker();
        private final String symbol;

        public GetStockBalance(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }

        @Override
        @Suspendable
        public Amount<TokenType> call() throws FlowException {
            TokenPointer<StockState> stockPointer = QueryUtilities.queryStockPointer(symbol, getServiceHub());
            Amount<TokenType> amount = QueryUtilitiesKt.tokenBalance(getServiceHub().getVaultService(), stockPointer);
            return amount;
        }
    }


    @InitiatingFlow
    @StartableByRPC
    public static class GetFiatBalance extends FlowLogic<Amount<TokenType>> {
        private final ProgressTracker progressTracker = new ProgressTracker();
        private final String currencyCode;

        public GetFiatBalance(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        @Override
        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }

        @Override
        @Suspendable
        public Amount<TokenType> call() throws FlowException {
            TokenType fiatTokenType = FiatCurrency.Companion.getInstance(currencyCode);
            Amount<TokenType> amount = QueryUtilitiesKt.tokenBalance(getServiceHub().getVaultService(), fiatTokenType);
            return amount;
        }
    }

}
