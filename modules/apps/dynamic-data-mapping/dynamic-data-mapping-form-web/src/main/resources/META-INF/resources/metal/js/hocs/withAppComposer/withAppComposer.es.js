import Component from 'metal-jsx';

/**
 * Higher-Order Component
 * @param {!Object} WrappedComponent
 * @return {!Object} new component
 */

const withAppComposer = WrappedComponent => {

	/**
	 * With App Composer.
	 * @extends Component
	 */

	class WithAppComposer extends Component {

		/*
         * Listen to the context change of your child component.
         * @param {!Object} context
         * @private
         */

		_handlePagesChanged(event) {
		}

		/**
		 * @inheritDoc
		 */

		render() {
			const {children} = this.props;
			const events = {
				pagesChanged: this._handlePagesChanged.bind(this)
			};

			const props = Object.assign({}, {...this.otherProps(), events});

			return <WrappedComponent {...props}>{children}</WrappedComponent>;
		}
	}

	return WithAppComposer;
};

export default withAppComposer;