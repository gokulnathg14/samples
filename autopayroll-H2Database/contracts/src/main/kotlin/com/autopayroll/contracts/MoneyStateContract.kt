package com.autopayroll.contracts

import com.autopayroll.states.MoneyState
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.Requirements.using
import net.corda.core.contracts.requireSingleCommand
import net.corda.core.contracts.requireThat
import net.corda.core.transactions.LedgerTransaction

// ************
// * Contract *
// ************
class MoneyStateContract : Contract {
    companion object {
        // Used to identify our contract when building a transaction.
        const val ID = "com.autopayroll.contracts.MoneyStateContract"
    }

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    override fun verify(tx: LedgerTransaction) {
        // Verification logic goes here.
        val cmd = tx.commands.requireSingleCommand<Commands>()
        when(cmd.value){
            is Commands.Pay -> requireThat {
                val output = tx.outputsOfType<MoneyState>().single()
                "Money payment is positive" using (output.amount > 0)
            }
        }
    }

    // Used to indicate the transaction's intent.
    interface Commands : CommandData {
        class Pay : Commands
    }
}