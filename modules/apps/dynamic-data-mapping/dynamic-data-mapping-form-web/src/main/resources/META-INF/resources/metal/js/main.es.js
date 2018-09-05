import {Config} from 'metal-state';
import {pageStructure} from './util/config.es';
import Builder from './pages/builder/index.es';
import Component from 'metal-jsx';
import dom from 'metal-dom';
import LayoutProvider from './components/LayoutProvider/index.es';
import loader from './components/FieldsLoader/index.es';
import RuleBuilder from './pages/RuleBuilder/index.es';
import withAppComposer from './hocs/withAppComposer/index.es';
import {EventHandler} from 'metal-events';

const STR_UNTITLED_FORM = Liferay.Language.get('untitled-form');

const LayoutProviderWithAppComposer = withAppComposer(LayoutProvider);

/**
 * Form.
 * @extends Component
 */

class Form extends Component {
	static PROPS = {

		/**
		 * The context for rendering a layout that represents a form.
		 * @default undefined
		 * @instance
		 * @memberof Form
		 * @type {!array}
		 */

		context: Config.shapeOf(
			{
				pages: Config.arrayOf(pageStructure),
				rules: Config.array()
			}
		).required().setter('_setContext'),

		/**
		 * The context for rendering a layout that represents a form.
		 * @default undefined
		 * @instance
		 * @memberof Form
		 * @type {!array}
		 */

		defaultLanguageId: Config.string().value(themeDisplay.getDefaultLanguageId()),

		/**
		 * The context for rendering a layout that represents a form.
		 * @default undefined
		 * @instance
		 * @memberof Form
		 * @type {!array}
		 */

		localizedDescription: Config.object().value({}),

		/**
		 * The context for rendering a layout that represents a form.
		 * @default undefined
		 * @instance
		 * @memberof Form
		 * @type {!array}
		 */

		localizedName: Config.object().value({}),

		/**
		 * The namespace of the portlet.
		 * @default undefined
		 * @instance
		 * @memberof Form
		 * @type {!string}
		 */

		namespace: Config.string().required(),

		/**
		 * The rules of a form.
		 * @default undefined
		 * @instance
		 * @memberof Form
		 * @type {!array}
		 */

		rules: Config.array(),

		/**
		 * The path to the SVG spritemap file containing the icons.
		 * @default undefined
		 * @instance
		 * @memberof Form
		 * @type {!string}
		 */

		spritemap: Config.string().required()
	};

	static STATE = {

		/**
		 * The represent the current active screen mode where 0 => FormBuilder and 1 => RuleBuilder
		 * @default undefined
		 * @instance
		 * @memberof Form
		 * @type {!array}
		 */

		activeFormMode: Config.number().value(0),

		saveButtonLabel: Config.string().value(Liferay.Language.get('save-form'))
	}

	_getFormContext(pages) {
		return {...this.props.context, pages};
	}

	_setContext(context) {
		if (!context.pages.length) {
			context = {
				...context,
				pages: [
					{
						description: '',
						rows: [],
						title: ''
					}
				]
			};
		}

		return context;
	}

	_getLocalizedName() {
		const {
			defaultLanguageId,
			localizedName
		} = this.props;

		if (!localizedName[defaultLanguageId].trim()) {
			localizedName[defaultLanguageId] = STR_UNTITLED_FORM;
		}

		return localizedName;
	}

	_getSerializedFormBuilderContext() {
		return JSON.stringify(this.getState());
	}

	/*
	 * Handles "pagesChanged" event. Updates hidden input with serialized From Builder context.
	 * @param {!Event} event
	 * @private
	 */

	_handlePagesChanged(event) {
		this.refs.serializedFormBuilderContextInput.value = JSON.stringify(this._getFormContext(event.newVal));
	}

	/**
	 * Handles click on save button. Saves Form when clicked.
	 * @param {!Event} event
	 * @private
	 */

	_handleSaveButtonClicked(event) {
		event.preventDefault();

		this.setState(
			{
				saveButtonLabel: Liferay.Language.get('saving')
			}
		);

		this.submitForm();
	}

	/**
	 * Handles click on plus button. Button shows Sidebar when clicked.
	 * @private
	 */

	_handleAddFieldButtonClicked() {
		const {builder} = this.refs;

		if (builder) {
			const {sidebar} = builder.refs;

			sidebar.props.mode = 'add';
			sidebar.show();
		}
	}

	created() {
		this._eventHandler = new EventHandler();
	}

	attached() {
		this._eventHandler.add(
			dom.on('#addFieldButton', 'click', this._handleAddFieldButtonClicked.bind(this)),
			dom.on('.forms-management-bar li', 'click', this._handleFormNavClicked.bind(this))
		);
	}

	_handleFormNavClicked(event) {
		const {delegateTarget, target} = event;
		const {navItemIndex} = delegateTarget.dataset;
		const addButton = document.querySelector('#addFieldButton');
		const formBuilderButtons = document.querySelector('.ddm-form-builder-buttons');
		const publishIcon = document.querySelector('.publish-icon');
		if (navItemIndex !== this.state.activeFormMode) {
			this.setState(
				{
					activeFormMode: parseInt(navItemIndex, 10)
				}
			);
			document.querySelector('.forms-management-bar li>a.active').classList.remove('active');
			if (parseInt(this.state.activeFormMode, 10)) {
				formBuilderButtons.classList.add('hide');
				publishIcon.classList.add('hide');
			}
			else {
				formBuilderButtons.classList.remove('hide');
				addButton.classList.remove('hide');
				publishIcon.classList.remove('hide');
			}
			target.classList.add('active');
		}
	}

	getState() {
		const {
			context,
			defaultLanguageId,
			localizedDescription,
			namespace
		} = this.props;
		const translationManager = Liferay.component(`${namespace}translationManager`);

		return {
			availableLanguageIds: translationManager && translationManager.get('availableLocales'),
			defaultLanguageId,
			description: localizedDescription,
			name: this._getLocalizedName(),
			pages: context.pages
		};
	}

	submitForm() {
		const {namespace} = this.props;

		this.syncInputValues();

		submitForm(document.querySelector(`#${namespace}editForm`));
	}

	syncInputValues() {
		const state = this.getState();
		const {
			description,
			name
		} = state;

		this.refs.descriptionInput.value = JSON.stringify(description);
		this.refs.nameInput.value = JSON.stringify(name);
		this.refs.serializedFormBuilderContextInput.value = this._getSerializedFormBuilderContext();
	}

	/**
	 * @inheritDoc
	 */

	render() {
		const {
			context,
			namespace,
			spritemap
		} = this.props;

		const {
			saveButtonLabel
		} = this.state;

		const events = {
			pagesChanged: this._handlePagesChanged.bind(this)
		};

		const layoutProviderProps = {
			...this.props,
			pages: context.pages
		};

		let mode = <Builder events={events} namespace={this.props.namespace} ref="builder" />;
		if (parseInt(this.state.activeFormMode, 10)) {
			mode = <RuleBuilder pages={context.pages} rules={this.props.rules} spritemap={spritemap} />;
		}

		return (
			<div>
				<input name={`${namespace}description`} ref="descriptionInput" type="hidden" />
				<input name={`${namespace}name`} ref="nameInput" type="hidden" />
				<input name={`${namespace}serializedFormBuilderContext`} ref="serializedFormBuilderContextInput" type="hidden" value={this._getSerializedFormBuilderContext()} />
				<input name={`${namespace}serializedSettingsContext`} ref="serializedSettingsContextInput" type="hidden" />

				<LayoutProviderWithAppComposer {...layoutProviderProps}>
					{mode}
				</LayoutProviderWithAppComposer>

				<div class="container-fluid-1280">
					<div class="button-holder ddm-form-builder-buttons">
						<button class="btn btn-primary ddm-button btn-default" ref="publishButton" type="button">
							{Liferay.Language.get('publish-button')}
						</button>
						<button class="btn ddm-button btn-default" data-onclick="_handleSaveButtonClicked" ref="saveButton">
							{saveButtonLabel}
						</button>
						<button class="btn ddm-button btn-link" ref="previewButton" type="button">
							{Liferay.Language.get('preview-form')}
						</button>
					</div>
				</div>
			</div>
		);
	}
}

const DDMForm = (props, container, callback) => {
	loader(
		() => callback(new Form(props, container)),
		props.modules,
		[...props.dependencies]
	);
};

export default DDMForm;
export {DDMForm};